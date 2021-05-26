### bugs-life-project

# Project for WIA1002

***Very important, finish reading before start using the application***

# JavaFX

To run JavaFX,
Go To Maven-> Plugins -> javafx -> javafx:run

If you have problem starting JavaFX, make sure to run Plugins -> clean -> clean:clean

![image](https://user-images.githubusercontent.com/18496769/119246939-4f3e9000-bb75-11eb-9db1-9b2a54c4f3a9.png)

Also, you can set run configurations to simplify the process.

For Intellij, Go To Run -> Edit Configuration -> Add new configuration -> Select Maven ->Set VM arguments ->javafx:run

# Database Option

You can also choose to use local database(fast) or online database(slow)

In MySQLOperation class, comment the part that you don't want to use.
![image](https://user-images.githubusercontent.com/18496769/119681217-64474780-be31-11eb-9e92-6febd090798d.png)

For online database, make sure you have a stable internet connection.

For local database, follow the steps below.

1. Download XAMPP for your reespective OS and install it.
   https://www.apachefriends.org/index.html
   ![image](https://user-images.githubusercontent.com/18496769/119681848-f2233280-be31-11eb-880a-d4503f2ccc08.png)

2. Open XAMPP and start Apache web service and MySQL
   ![image](https://user-images.githubusercontent.com/18496769/119682309-51814280-be32-11eb-8080-217147b639ad.png)

3. Go to http://localhost/phpmyadmin/index.php?route=/server/databases
![image](https://user-images.githubusercontent.com/18496769/119682707-a02edc80-be32-11eb-99fc-c2a2d7b3cadb.png)

4. Create Database with name ***"bugs_life"***, click create
![image](https://user-images.githubusercontent.com/18496769/119682957-d1a7a800-be32-11eb-8e65-d4300867fefb.png)

5. Select bugs_life database and choose import
   Then, select **"bugs_life.sql"**
   ![image](https://user-images.githubusercontent.com/18496769/119683304-10d5f900-be33-11eb-9231-2cbb7ee869bf.png)
   
6. Click Go (At the bottom right of the screen)

8. You are good to go! Local Database have been set up!


   



