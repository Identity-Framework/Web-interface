declare default element namespace "http://esterline.ncat.edu/dsTheory.xsd";
declare variable $doc external;
declare variable $sID external;
declare variable $feature external;
declare variable $mID external;

let $x := $doc/DempsterShaferTheory/DSInstance[@scenarioID=$sID]/EvidenceModality[@type=$feature]
return $x/massModificationTemplate[@id=$mID]/filename/text()