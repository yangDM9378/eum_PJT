import GroupInfo from "@/components/map/GroupInfo";
import MapUpper from "@/components/map/MapUpper";
import React from "react";

export default function MapPage({ params }: { params: { slug: number } }) {
  console.log(params.slug)
  return (
    <>
      <GroupInfo />
      <MapUpper groupId = {params.slug}/>
    </>
  );
}
