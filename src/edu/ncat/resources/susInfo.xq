declare default element namespace "http://http://esterline.ncat.edu/webInterface.xsd";
declare variable $doc external;
declare variable $ind external;
declare variable $sID external;

let $x := $doc/Scenarios/Scenario[@id=$sID]/Suspects/Suspect
return ($x[$ind]/suspectName/text(), $x[$ind]/suspectQuery/text())