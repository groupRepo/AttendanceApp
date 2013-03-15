<?php
$un=$_POST['uID'];
$pw=$_POST['uPWord'];

$db_host = "localhost";
$db_uid = "mmi";
$db_pass = "mmi";
$db_name = "markmein";
$db_con = mysql_connect($db_host,$db_uid,$db_pass) or die('could not connect');
mysql_select_db($db_name);

/*
* These 3 selects are for Overall Attendance
* 1.Gets count of students in a given module
* 2.Gets count of times a module attendance has been recorded
* 3.Gets count of all students that attended a given module over time.
* (3/(1*2)) * 100 gives percentage
*/

$query_search1 = "SELECT COUNT(studentId) AS 'a' FROM StudentModules WHERE  moduleOfferingId = 'CRN8081'";
$result1 = mysql_query($query_search1);
while($row1=mysql_fetch_assoc($result1))
	$output[]=$row1["a"];

$query_search2 = "SELECT COUNT(moduleOfferingId) AS 'b' FROM Attendances WHERE moduleOfferingId = 'CRN8081'";
$result2 = mysql_query($query_search2);
while($row2=mysql_fetch_assoc($result2))
	$output[]=$row2;

$query_search3 = "SELECT COUNT(studentId) AS 'c' FROM StudentAttendances s, Attendances a WHERE s.attendanceId = a.id AND a.moduleOfferingId = 'CRN8081'";
$result3 = mysql_query($query_search3);
while($row3=mysql_fetch_assoc($result3))
	$output[]=$row3;

/*
* These 2 selects are for Lecture Attendance
* 4.Gets count of times a module attendance has been recorded for lectures S
* 5.Gets count of all students that attended a given module over time for lectures S
* (5/(1*4)) * 100 gives percentage
*/

$query_search4 = "SELECT COUNT(moduleOfferingId) AS 'd' FROM Attendances WHERE moduleOfferingId = 'CRN8081' AND Type = 'S'";
$result4 = mysql_query($query_search4);
while($row4=mysql_fetch_assoc($result4))
	$output[]=$row4;

$query_search5 = "SELECT COUNT(studentId) AS 'e' FROM StudentAttendances s, Attendances a WHERE s.attendanceId = a.id AND a.moduleOfferingId = 'CRN8081' AND a.Type = 'S'";
$result5 = mysql_query($query_search5);
while($row5=mysql_fetch_assoc($result5))
	$output[]=$row5;

/*
 * These 2 selects are for Lab Attendance
* 4.Gets count of times a module attendance has been recorded for Labs R
* 5.Gets count of all students that attended a given module over time for Labs R
* (5/(1*4)) * 100 gives percentage
*/

$query_search6 = "SELECT COUNT(moduleOfferingId) AS 'f' FROM Attendances WHERE moduleOfferingId = 'CRN8081' AND Type = 'R'";
$result6 = mysql_query($query_search6);
while($row6=mysql_fetch_assoc($result6))
	$output[]=$row6;

$query_search7 = "SELECT COUNT(studentId) AS 'g' FROM StudentAttendances s, Attendances a WHERE s.attendanceId = a.id AND a.moduleOfferingId = 'CRN8081' AND a.Type = 'R'";
$result7 = mysql_query($query_search7);
while($row7=mysql_fetch_assoc($result7))
	$output[]=$row7;

/*
 * These 2 selects are for Best and Worst Attending Students
* 4.Gets count of times a module attendance has been recorded for Labs R
* 5.Gets count of all students that attended a given module over time for Labs R
* (5/(1*4)) * 100 gives percentage
*/

$query_search8 = "SELECT name AS 'h' FROM Users WHERE id = (SELECT studentId FROM StudentAttendances s, Attendances a WHERE  s.attendanceId = a.id AND a.moduleOfferingId = 'CRN8081' GROUP BY studentId ORDER BY studentId ASC LIMIT 1)";
$result8 = mysql_query($query_search8);
while($row8=mysql_fetch_assoc($result8))
	$output[]=$row8;

$query_search9 = "SELECT name AS 'i' FROM Users WHERE id = (SELECT studentId FROM StudentAttendances s, Attendances a WHERE  s.attendanceId = a.id AND a.moduleOfferingId = 'CRN8081' GROUP BY studentId ORDER BY studentId DESC LIMIT 1)";
$result9 = mysql_query($query_search9);
while($row9=mysql_fetch_assoc($result9))
	$output[]=$row9;

/*while($row1=mysql_fetch_assoc($result1))
 $a = $row1["noStu"];

while($row2=mysql_fetch_assoc($result2))
	$b = $row2["noAtt"];

while($row3=mysql_fetch_assoc($result3)){
$c =  $row3["actAtt"];
}

print $c/($a*$b);*/

print(json_encode($output));
echo $output;

mysql_close();
?>

