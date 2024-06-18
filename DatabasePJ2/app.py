from flask import Flask, render_template, request, redirect, url_for, session, flash
from werkzeug.security import generate_password_hash, check_password_hash
from datetime import datetime, timedelta
from markupsafe import Markup
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import text
import json
import pymysql



app = Flask(__name__)
app.secret_key = 'dongli5576'
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:dongli5576@localhost/pj2'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)



def sanitize_input(input_string):
    malicious_keywords = ["script", "SELECT", "INSERT", "DELETE", "UPDATE",
                          "DROP", "select", "insert", "delete", "update",
                          "drop", "--", ";", "/*", "*/", "@@", "char",
                          "<script>", "</script>", "alert", "javascript"]
    for word in malicious_keywords:
        input_string = input_string.replace(word, "")
    return input_string


@app.route('/', methods=['GET', 'POST'])
def index():
    return render_template('index.html')


@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        # Collect form data
        username = sanitize_input(request.form['username'])
        password = sanitize_input(request.form['password'])
        fName = sanitize_input(request.form['fName'])
        lName = sanitize_input(request.form['lName'])
        billing_addr = sanitize_input(request.form['b_addr'])
        billing_zcode = sanitize_input(request.form['b_zcode'])
        hashed_password = generate_password_hash(password)

        try:
            # Check if username exists
            user_check_query = text("SELECT * FROM User WHERE username = :username")
            user_check_result = db.session.execute(user_check_query, {'username': username}).fetchone()
            if user_check_result:
                flash("Registration failed: Username already taken.", "error")
                return redirect(url_for('index'))

            # Insert new user
            insert_user_query = text("INSERT INTO User (username, hashed_pwd) VALUES (:username, :hashed_password)")
            db.session.execute(insert_user_query, {'username': username, 'hashed_password': hashed_password})
            db.session.commit()

            # Get the user_id of the new user
            user_id_query = text("SELECT LAST_INSERT_ID()")
            user_id = db.session.execute(user_id_query).scalar()

            # Insert into Customer table
            insert_customer_query = text("INSERT INTO Customer (uid, first_name, last_name, billing_addr, billing_zcode) VALUES (:user_id, :fName, :lName, :billing_addr, :billing_zcode)")
            db.session.execute(insert_customer_query, {'user_id': user_id, 'fName': fName, 'lName': lName, 'billing_addr': billing_addr, 'billing_zcode': billing_zcode})
            db.session.commit()

            flash("Registration successful! Please login.", "success")
            return redirect(url_for('index'))
        except Exception as e:
            flash(f"Error during registration: {e}", "error")
            db.session.rollback()

    return render_template('register.html')


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = sanitize_input(request.form['username'])
        print(username)
        password = sanitize_input(request.form['password'])

        try:
            query = text("SELECT uid, hashed_pwd FROM User WHERE username = :username")
            result = db.session.execute(query, {'username': username}).fetchone()

            if result and check_password_hash(result.hashed_pwd, password):
                session['user_id'] = result.uid
                flash("Logged in successfully!", "success")
                return redirect(url_for('dashboard'))
            else:
                flash("Login failed. Please check your credentials.", "error")
                return redirect(url_for('index'))

        except Exception as e:
            flash(f"An error occurred: {e}", "error")

    return render_template('login.html')


@app.route('/dashboard')
def dashboard():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    user_id = session['user_id']
    locations = []

    try:
        query = text("SELECT cid FROM Customer WHERE uid = :user_id")
        result = db.session.execute(query, {'user_id': user_id}).mappings().first()

        if result:
            cid = result['cid']  # Accessing the result as a dictionary
            locations_query = text("SELECT * FROM Location WHERE cid = :cid AND is_active = 1")
            locations = db.session.execute(locations_query, {'cid': cid}).mappings().all()
        else:
            flash("Customer ID not found", "error")
            return redirect(url_for('login'))
    except Exception as e:
        flash(f"An error occurred: {e}", "error")

    return render_template('dashboard.html', locations=locations)


@app.route('/add_location', methods=['GET', 'POST'])
def add_location():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    uid = session['user_id']

    if request.method == 'POST':
        zcode = sanitize_input(request.form['zcode'])
        address = sanitize_input(request.form['address'])
        area = sanitize_input(request.form['area'])
        num_bedrooms = sanitize_input(request.form['num_bedrooms'])
        num_occupants = sanitize_input(request.form['num_occupants'])

        try:
            cid_query = text("SELECT cid FROM Customer WHERE uid = :uid")
            cid_result = db.session.execute(cid_query, {'uid': uid}).mappings().first()

            if cid_result:
                cid = cid_result['cid']
                location_query = text(
                    "INSERT INTO Location (cid, zcode, address, area, num_bedrooms, num_occupants) VALUES (:cid, :zcode, :address, :area, :num_bedrooms, :num_occupants)")
                db.session.execute(location_query, {'cid': cid, 'zcode': zcode, 'address': address, 'area': area,
                                                    'num_bedrooms': num_bedrooms, 'num_occupants': num_occupants})
                db.session.commit()
                flash('Location added successfully!', 'success')
                return redirect('dashboard')
            else:
                flash('Customer ID not found for user.', 'error')

        except Exception as e:
            flash(f'An error occurred while adding the location: {e}', 'error')
            db.session.rollback()

    return render_template('add_location.html')  # Ensure there's a return statement for all paths


@app.route('/my_locations')
def my_locations():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    uid = session['user_id']
    locations = []

    try:
        locations_query = text("SELECT * FROM Location WHERE cid = (SELECT cid FROM Customer WHERE uid = :uid) AND is_active = 1")
        locations = db.session.execute(locations_query, {'uid': uid}).mappings().all()
    except Exception as e:
        flash(f'An error occurred: {e}', 'error')

    return render_template('my_locations.html', locations=locations)


@app.route('/deactivate_location/<int:lid>', methods=['POST'])
def deactivate_location(lid):
    if 'user_id' not in session:
        flash("Please log in to continue.", "error")
        return redirect(url_for('login'))

    uid = session['user_id']

    try:
        # Check for active devices in the location
        active_devices_query = text("""
            SELECT COUNT(*) 
            FROM Device 
            WHERE lid = :lid AND is_active = 1
        """)
        active_devices_count = db.session.execute(active_devices_query, {'lid': lid}).scalar()

        if active_devices_count > 0:
            flash('Cannot delete location with active devices.', 'error')
            return redirect(url_for('dashboard'))

        # If no active devices, proceed with deactivation
        deactivate_query = text("UPDATE Location SET is_active = 0 WHERE lid = :lid AND cid = (SELECT cid FROM Customer WHERE uid = :uid)")
        db.session.execute(deactivate_query, {'lid': lid, 'uid': uid})
        db.session.commit()
        flash('Location deleted successfully.', 'success')
    except Exception as e:
        flash(f'An error occurred: {e}', 'error')
        db.session.rollback()

    return redirect(url_for('dashboard'))


@app.route('/add_device', methods=['GET', 'POST'])
def add_device():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    uid = session['user_id']

    if request.method == 'POST':
        lid = sanitize_input(request.form['lid'])
        dtype = sanitize_input(request.form['dtype'])
        dmodel = sanitize_input(request.form['dmodel'])
        description = sanitize_input(request.form['description'])

        try:
            device_query = text("INSERT INTO Device (lid, dtype, dmodel, description) VALUES (:lid, :dtype, :dmodel, :description)")
            db.session.execute(device_query, {'lid': lid, 'dtype': dtype, 'dmodel': dmodel, 'description': description})
            db.session.commit()
            flash("Device added successfully!", "success")
        except Exception as e:
            flash(f"Error adding device: {e}", "error")
            db.session.rollback()

        return redirect(url_for('dashboard'))

    # Fetch locations for the user
    locations_query = text("SELECT lid, address FROM Location WHERE cid = (SELECT cid FROM Customer WHERE uid = :uid) AND is_active = 1")
    locations = db.session.execute(locations_query, {'uid': uid}).fetchall()
    return render_template('add_device.html', locations=locations)


@app.route('/my_devices')
def my_devices():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    uid = session['user_id']

    try:
        devices_query = text("""
            SELECT d.did, l.address, d.dtype, d.dmodel, d.description 
            FROM Device d 
            JOIN Location l ON d.lid = l.lid 
            JOIN Customer c ON l.cid = c.cid 
            WHERE c.uid = :uid AND d.is_active = 1
        """)
        devices = db.session.execute(devices_query, {'uid': uid}).fetchall()
    except Exception as e:
        flash(f'An error occurred: {e}', 'error')

    no_devices = len(devices) == 0
    return render_template('my_devices.html', devices=devices, no_devices=no_devices)


@app.route('/deactivate_device/<int:did>', methods=['POST'])
def deactivate_device(did):
    if 'user_id' not in session:
        return redirect(url_for('login'))

    uid = session['user_id']

    try:
        deactivate_device_query = text("""
            UPDATE Device d
            JOIN Location l ON d.lid = l.lid
            JOIN Customer c ON l.cid = c.cid
            SET d.is_active = 0
            WHERE c.uid = :uid AND d.did = :did
        """)
        db.session.execute(deactivate_device_query, {'uid': uid, 'did': did})
        db.session.commit()
        flash("Device deactivated successfully!", "success")
    except Exception as e:
        flash(f'An error occurred: {e}', 'error')
        db.session.rollback()

    return redirect(url_for('my_devices'))


@app.route('/logout')
def logout():
    session.clear()  # Clear the user's session
    return redirect(url_for('index'))  # Redirect to the home page or login page


@app.route('/energy_consumption')
def energy_consumption():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    uid = session['user_id']
    start_date = request.args.get('start', (datetime.today() - timedelta(days=30)).strftime('%Y-%m-%d'))
    end_date = request.args.get('end', datetime.today().strftime('%Y-%m-%d'))

    try:
        query = text("""
            SELECT DATE(eu.utimestamp) as date, SUM(eu.kWh) as total_usage
            FROM EnergyUsage eu
            JOIN Device d ON eu.did = d.did
            JOIN Location l ON d.lid = l.lid
            JOIN Customer c ON l.cid = c.cid
            WHERE c.uid = :uid AND eu.utimestamp BETWEEN :start_date AND :end_date
            GROUP BY DATE(eu.utimestamp)
            ORDER BY DATE(eu.utimestamp)
        """)
        result = db.session.execute(query, {'uid': uid, 'start_date': start_date, 'end_date': end_date}).fetchall()

        if not result:
            flash("No energy usage data found for the given date range.", "info")

        dates = [row[0].strftime("%Y-%m-%d") for row in result]
        usages = [float(row[1]) for row in result]
        print(dates)
        print(usages)

        return render_template('energy_consumption.html',
                               dates=Markup(json.dumps(dates)),
                               usages=Markup(json.dumps(usages)),
                               start_date=start_date,
                               end_date=end_date)
    except Exception as e:
        flash(f"An error occurred: {e}", "error")
        return render_template('energy_consumption.html',
                               dates=Markup(json.dumps([])),
                               usages=Markup(json.dumps([])),
                               start_date=start_date,
                               end_date=end_date)


from flask import flash, render_template, redirect, url_for, session
from sqlalchemy import text
from datetime import datetime, timedelta

@app.route('/energy_per_device', methods=['GET', 'POST'])
def energy_per_device():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    uid = session['user_id']
    default_start_date = (datetime.today() - timedelta(days=30)).strftime('%Y-%m-%d')
    default_end_date = datetime.today().strftime('%Y-%m-%d')

    if request.method == 'POST':
        start_date_str = request.form.get('start_date', default_start_date)
        end_date_str = request.form.get('end_date', default_end_date)
    else:
        start_date_str = default_start_date
        end_date_str = default_end_date

    start_date = datetime.strptime(start_date_str, '%Y-%m-%d')
    end_date = datetime.strptime(end_date_str, '%Y-%m-%d')

    try:
        # Detailed data for the table
        detailed_query = text("""
            SELECT l.address, d.dtype, d.dmodel, SUM(eu.kWh) as total_energy
            FROM EnergyUsage eu
            JOIN Device d ON eu.did = d.did
            JOIN Location l ON d.lid = l.lid
            JOIN Customer c ON l.cid = c.cid
            WHERE c.uid = :uid AND eu.utimestamp BETWEEN :start_date AND :end_date
            GROUP BY l.address, d.dtype, d.dmodel
        """)
        detailed_result = db.session.execute(detailed_query, {'uid': uid, 'start_date': start_date, 'end_date': end_date}).fetchall()

        # Aggregated data for the pie chart
        aggregated_query = text("""
            SELECT d.dtype, SUM(eu.kWh) as total_energy
            FROM EnergyUsage eu
            JOIN Device d ON eu.did = d.did
            JOIN Location l ON d.lid = l.lid
            JOIN Customer c ON l.cid = c.cid
            WHERE c.uid = :uid AND eu.utimestamp BETWEEN :start_date AND :end_date
            GROUP BY d.dtype
        """)
        aggregated_result = db.session.execute(aggregated_query, {'uid': uid, 'start_date': start_date, 'end_date': end_date}).fetchall()

        device_types_chart = [device.dtype for device in aggregated_result]
        energy_consumption_chart = [float(device.total_energy) for device in aggregated_result]

        no_data = len(detailed_result) == 0

        return render_template('energy_per_device.html',
                               device_energy=detailed_result,
                               device_types_chart=device_types_chart,
                               energy_consumption_chart=energy_consumption_chart,
                               start_date=start_date_str,
                               end_date=end_date_str,
                               no_data=no_data)
    except Exception as e:
        flash(f"An error occurred: {e}", "error")
        return render_template('energy_per_device.html',
                               device_energy=[],
                               device_types_chart=[],
                               energy_consumption_chart=[],
                               start_date=start_date_str,
                               end_date=end_date_str,
                               no_data=True)


@app.route('/compare_with_similar_locations', methods=['GET', 'POST'])
def compare_with_similar_locations():
    if 'user_id' not in session:
        flash("Please log in to view this page.", "error")
        return redirect(url_for('login'))

    uid = session['user_id']
    comparison_data = []

    if request.method == 'POST':
        start_date = request.form['start_date']
        end_date = request.form['end_date']
        area_percentage = float(request.form['area_percentage']) / 100

        try:
            query = text("""
                WITH LocEU AS (
                    SELECT l.lid, l.address, l.area, SUM(eu.kWh) AS loc_eu
                    FROM EnergyUsage AS eu
                    JOIN Device AS d ON eu.did = d.did
                    JOIN Location AS l ON d.lid = l.lid
                    WHERE eu.utimestamp BETWEEN :start_date AND :end_date
                    GROUP BY l.lid, l.address, l.area
                ),
                CompEU AS (
                    SELECT l1.lid, l1.address, l1.area, l1.loc_eu, ROUND(AVG(l2.loc_eu), 2) AS avg_eu,
                           ROUND(l1.loc_eu / AVG(l2.loc_eu) * 100, 2) AS UsagePercentage
                    FROM LocEU AS l1
                    JOIN LocEU AS l2 ON l1.lid != l2.lid 
                                       AND l2.area BETWEEN l1.area * (1 - :area_percentage) AND l1.area * (1 + :area_percentage)
                    GROUP BY l1.lid, l1.address, l1.area, l1.loc_eu
                )
                SELECT * FROM CompEU WHERE lid = (
                    SELECT cid FROM Customer WHERE uid = :uid
                )
                ORDER BY lid;
            """)
            result = db.session.execute(query, {
                'start_date': start_date,
                'end_date': end_date,
                'area_percentage': area_percentage,
                'uid': uid
            })
            comparison_data = result.fetchall()

        except Exception as e:
            flash(f'An error occurred: {e}', 'error')

    return render_template('compare_with_similar_locations.html', comparison_data=comparison_data)


@app.route('/abnormal_events', methods=['GET', 'POST'])
def abnormal_events():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    uid = session['user_id']
    events = []

    # Default dates
    start_date = request.args.get('start', (datetime.today() - timedelta(days=30)).strftime('%Y-%m-%d'))
    end_date = request.args.get('end', datetime.today().strftime('%Y-%m-%d'))

    try:
        query = text("""
            SELECT e1.etimestamp as DoorOpenedTime, 
                   MIN(e2.etimestamp) as DoorClosedTime,
                   l.lid as LocationID,
                   l.address as ServiceLocation,
                   d.did as DeviceID,
                   d.dmodel as RefrigeratorModel 
            FROM Event as e1 
            JOIN Device as d on e1.did = d.did 
            JOIN Location as l on d.lid = l.lid 
            JOIN Customer as c on l.cid = c.cid
            LEFT JOIN Event as e2 on e1.did = e2.did 
                AND e2.etype = 'door closed'
                AND e2.etimestamp > e1.etimestamp
            WHERE c.uid = :uid 
                AND e1.etype = 'door opened'
                AND d.dtype = 'refrigerator'
                AND e1.etimestamp BETWEEN :start_date AND :end_date
            GROUP BY e1.did, e1.etimestamp, l.lid, l.address, d.did, d.dmodel 
            HAVING MIN(e2.etimestamp) is NULL OR MIN(e2.etimestamp) > DATE_ADD(e1.etimestamp, INTERVAL 30 MINUTE)
        """)
        result = db.session.execute(query, {'uid': uid, 'start_date': start_date, 'end_date': end_date})
        events = result.fetchall()
    except Exception as e:
        flash(f'An error occurred: {e}', 'error')

    return render_template('abnormal_events.html', events=events, start_date=start_date, end_date=end_date)


@app.route('/usage_increase_comparison')
def usage_increase_comparison():
    if 'user_id' not in session:
        flash("Please log in to access this page.", "error")
        return redirect(url_for('login'))

    uid = session['user_id']

    try:
        # User's own service location's percentage increase
        user_increase_query = text("""
            WITH AugUsage AS (
                SELECT l.lid, l.address, SUM(eu.kWh) AS AugustUsage
                FROM EnergyUsage AS eu
                JOIN Device AS d ON eu.did = d.did
                JOIN Location AS l ON d.lid = l.lid
                JOIN Customer AS c ON l.cid = c.cid
                WHERE c.uid = :uid AND eu.utimestamp BETWEEN '2022-08-01' AND '2022-08-31'
                GROUP BY l.lid, l.address
            ),
            SeptUsage AS (
                SELECT l.lid, SUM(eu.kWh) AS SeptemberUsage
                FROM EnergyUsage AS eu
                JOIN Device AS d ON eu.did = d.did
                JOIN Location AS l ON d.lid = l.lid
                JOIN Customer AS c ON l.cid = c.cid
                WHERE c.uid = :uid AND eu.utimestamp BETWEEN '2022-09-01' AND '2022-09-30'
                GROUP BY l.lid
            )
            SELECT a.address, CASE WHEN a.AugustUsage > 0 THEN (s.SeptemberUsage - a.AugustUsage) / a.AugustUsage * 100 ELSE NULL END AS PercentageIncrease
            FROM AugUsage AS a
            JOIN SeptUsage AS s ON a.lid = s.lid
        """)
        user_increase = db.session.execute(user_increase_query, {'uid': uid}).fetchall()

        # Highest percentage increase across all locations
        highest_increase_query = text("""
            SELECT MAX(PercentageIncrease) AS MaxPercentageIncrease
            FROM (
                SELECT 
                    (SUM(CASE WHEN eu.utimestamp BETWEEN '2022-09-01' AND '2022-09-30' THEN eu.kWh ELSE 0 END) -
                     SUM(CASE WHEN eu.utimestamp BETWEEN '2022-08-01' AND '2022-08-31' THEN eu.kWh ELSE 0 END)) / 
                     GREATEST(SUM(CASE WHEN eu.utimestamp BETWEEN '2022-08-01' AND '2022-08-31' THEN eu.kWh ELSE 0 END), 1) * 100 AS PercentageIncrease
                FROM EnergyUsage AS eu
                JOIN Device AS d ON eu.did = d.did
                JOIN Location AS l ON d.lid = l.lid
                JOIN Customer AS c ON l.cid = c.cid
                GROUP BY l.lid
            ) AS MonthlyUsage
        """)

        highest_increase = db.session.execute(highest_increase_query).fetchone()

    except Exception as e:
        flash(f"An error occurred: {e}", "error")
        return render_template('usage_increase_comparison.html', user_increase=None, highest_increase=None)

    return render_template('usage_increase_comparison.html', user_increase=user_increase, highest_increase=highest_increase)


@app.route('/device_energy_cost')
def device_energy_cost():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    uid = session['user_id']

    try:
        query = text("""
            SELECT d.dmodel, l.address, DECView.TotalCost, DECView.PotentialSavings
            FROM DeviceEnergyCost DECView
            JOIN Device d ON DECView.did = d.did
            JOIN Location l ON d.lid = l.lid
            JOIN Customer c ON l.cid = c.cid
            WHERE c.uid = :uid
        """)
        result = db.session.execute(query, {'uid': uid}).fetchall()

        # Extracting data for the chart
        labels = [device.dmodel for device in result]  # Device models as labels
        total_costs = [device.TotalCost for device in result]  # Total costs
        potential_savings = [device.PotentialSavings for device in result]  # Potential savings

        return render_template('device_energy_cost.html',
                               device_costs=result,
                               labels=labels,
                               total_costs=total_costs,
                               potential_savings=potential_savings)
    except Exception as e:
        flash(f"An error occurred: {e}", "error")
        return render_template('device_energy_cost.html', device_costs=[])


if __name__ == '__main__':
    app.run()
