SELECT m.name, (
(
	SELECT COUNT( sa.studentId )
	FROM StudentAttendancessa, Attendances a
	WHERE sa.attendanceid = a.id
	AND a.moduleOfferingId = sm.moduleOfferingId
	AND sa.studentId = sm.studentId
	) / (
	SELECT COUNT( a.moduleOfferingId )
	FROM Attendances a
	WHERE a.moduleOfferingId = sm.moduleOfferingId ) *100
	) 
	AS 'attendance'
FROM Modules m, ModuleOfferingsmo, StudentModules sm
WHERE m.code = mo.moduleCode
AND sm.moduleOfferingId = mo.code
AND sm.studentId = 'R00068252'
LIMIT 0 , 30