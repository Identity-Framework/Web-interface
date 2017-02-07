for $x in doc("scenario.xml") /scenarios/scenario/name
order by $x
return data($x)