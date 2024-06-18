import pymysql

class Mysql(object):
    def __init__(self):
        try:
            self.conn = pymysql.connect(host='localhost', user='root',
                                        password='dongli5576', database='pj2', charset="utf8")
            self.cursor = self.conn.cursor()
            print("Successfully connected to MySQL!")
        except Exception as e:
            print("Failed to connect to MySQL...")
            print(e)

    def fetch_customers(self):
        try:
            sql = "select * from Customer"
            self.cursor.execute(sql)
            return self.cursor.fetchall()
        except Exception as e:
            print("SQL query failed...")
            print(e)
            return None






