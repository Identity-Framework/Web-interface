declare default element namespace "http://http://esterline.ncat.edu/webInterface.xsd";
declare variable $doc external;
declare variable $sID external;

let $x := $doc/Scenarios/Scenario
where $x[@id=$sID]
return data($x/description)