<?php
define('Host',"HOST");
define('User',"USER");
define('Pass',"PASSWORD");
define('Db',"DATABASE_NAME");



$con=mysqli_connect(Host,User,Pass,Db);
 $name = "test";  //like only enter the name of contact
    $contact = $_POST['contact'];         //get contact no. from app
if($con)
{
   
    
     $queryInsert="INSERT INTO Contact(Name,Contac)VALUES('$name', '$contact')";
     if(mysqli_query($con,$queryInsert)) 
     {
        echo("Inserted");
      } 
      else
      {
        echo("not inserted");
      }
   
	
	
   
}
else
{
echo("connection problem");
}


?>
