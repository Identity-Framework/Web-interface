declare default element namespace "http://esterline.ncat.edu/dsTheory.xsd";
declare variable $doc external;
declare variable $sID external;

let $x := $doc/DempsterShaferTheory/DSInstance[@scenarioID=$sID]
return data($x/EvidenceModality/@type)