import GroupInfo from "@/components/map/GroupInfo";
import MapUpper from "@/components/map/MapUpper";
import React from "react";

export default function MapPage({ params }: { params: { slug: number } }) {
  return (
    <>
      <GroupInfo groupId={params.slug} />
      <MapUpper groupId={params.slug} />
    </>
  );
}
