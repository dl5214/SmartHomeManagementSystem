from mysql import Mysql

mysql_instance = Mysql()
customers = mysql_instance.fetch_customers()

if customers is not None:
    for customer in customers:
        print(customer)