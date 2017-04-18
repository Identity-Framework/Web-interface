declare default element namespace "http://http://esterline.ncat.edu/webInterface.xsd";
declare variable $doc external;

for $x in $doc/Scenarios/Scenario
return data($x/name)