<?php
//database setup
$db_host = "localhost";
$db_uid = "mmi";
$db_pass = "mmi";
$db_name = "markmein";
$db_con = mysql_connect($db_host,$db_uid,$db_pass) or die('could not connect');
mysql_select_db($db_name);


$getAllOfferings = "SELECT code FROM ModuleOfferings";

$resultM = mysql_query($getAllOfferings);


while($infoM = mysql_fetch_array($resultM))
{
	$code = $infoM['code'];
	print "<br><b>Training class:</b> ".$code . "<br> ";

	$getAllStudents = "SELECT studentId FROM StudentModules WHERE moduleOfferingId = '$code'";
	$resultS = mysql_query($getAllStudents);

	$sCodes = array(20);
	$counter = 0;
	print "<ul>";
	while($infoS = mysql_fetch_array($resultS))
	{
		$student = $infoS['studentId'];
		$sCodes[$counter] = $student;
		$counter++;
		print "<li>  Student: ".$student . "</li> ";
	}
	print "</ul>";
	$command = "mmi/train.sh ".$code." ";
	foreach($sCodes as &$a){
		$command  = $command.$a." ";
	}
	print "Command: ".$command."<br>";
	$output = shell_exec($command);

	print $output;

}
mysql_close();
?>
