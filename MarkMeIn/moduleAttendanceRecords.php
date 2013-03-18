<?php
$code=$_POST['code'];

$db_host = "localhost";
$db_uid = "mmi";
$db_pass = "mmi";
$db_name = "markmein";
$db_con = mysql_connect($db_host,$db_uid,$db_pass) or die('could not connect');
mysql_select_db($db_name);

/*
 * 1.Gets count of overall attendance for module
* 2.Gets count of overall attendance for a modules labs
* 3.Gets count of overall attendance for a modules lectures
*/
$query_search1 = "SELECT ((SELECT COUNT( sa.attendanceId ) FROM Attendances a, StudentAttendances sa WHERE a.id = sa.attendanceId AND a.moduleOfferingId = mo.code ) / ( ( SELECT COUNT( studentId ) FROM StudentModules WHERE moduleOfferingId = mo.code ) * ( SELECT COUNT( id )  FROM Attendances WHERE moduleOfferingId = mo.code )) *100) AS  'overall' FROM ModuleOfferings mo WHERE mo.code =  '$code'";
$result1 = mysql_query($query_search1);
while($row1=mysql_fetch_assoc($result1)){
	$output[]=$row1;
}

$query_search2 = "SELECT ((SELECT COUNT( sa.attendanceId ) FROM Attendances a, StudentAttendances sa WHERE a.id = sa.attendanceId AND a.moduleOfferingId = mo.code AND a.type ='R') / ( ( SELECT COUNT( studentId ) FROM StudentModules WHERE moduleOfferingId = mo.code ) * ( SELECT COUNT( id )  FROM Attendances WHERE moduleOfferingId = mo.code )) *100) AS  'lab' FROM ModuleOfferings mo WHERE mo.code =  '$code'";
$result2 = mysql_query($query_search2);
while($row2=mysql_fetch_assoc($result2))
	$output[]=$row2;

$query_search3 = "SELECT ((SELECT COUNT( sa.attendanceId ) FROM Attendances a, StudentAttendances sa WHERE a.id = sa.attendanceId AND a.moduleOfferingId = mo.code AND a.type ='s') / ( ( SELECT COUNT( studentId ) FROM StudentModules WHERE moduleOfferingId = mo.code ) * ( SELECT COUNT( id )  FROM Attendances WHERE moduleOfferingId = mo.code )) *100) AS  'lecture' FROM ModuleOfferings mo WHERE mo.code =  '$code'";
$result3 = mysql_query($query_search3);
while($row3=mysql_fetch_assoc($result3))
	$output[]=$row3;

/*
 * These 2 selects are for Best and Worst Attending Students
* 4.Gets name of best student
* 5.Gets name of worst student
*/

$query_search4 = "SELECT name AS 'best' FROM Users WHERE id = (SELECT studentId FROM StudentAttendances s, Attendances a WHERE  s.attendanceId = a.id AND a.moduleOfferingId = 'CRN8081' GROUP BY studentId ORDER BY studentId ASC LIMIT 1)";
$result4 = mysql_query($query_search4);
while($row4=mysql_fetch_assoc($result4))
	$output[]=$row4;

$query_search5 = "SELECT name AS 'worst' FROM Users WHERE id = (SELECT studentId FROM StudentAttendances s, Attendances a WHERE  s.attendanceId = a.id AND a.moduleOfferingId = 'CRN8081' GROUP BY studentId ORDER BY studentId DESC LIMIT 1)";
$result5 = mysql_query($query_search5);
while($row5=mysql_fetch_assoc($result5))
	$output[]=$row5;

print(json_encode($output));
echo $output;

mysql_close();
?>

