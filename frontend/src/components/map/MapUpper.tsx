"use client";

import React from "react";
import { useLoadScript, LoadScriptProps } from "@react-google-maps/api";
import Map from "./Map";
import { getPinList } from "@/services/pinApi";
import { useQuery } from "@tanstack/react-query";
import { useAppDispatch } from "@/redux/hooks";
import { setGroupId } from "@/redux/map/mapSlice";

const libraries: LoadScriptProps["libraries"] = ["places"];
interface Props {
  groupId: number;
}
const MapUpper = ({ groupId }: Props) => {
  const MAP_API_KEY = process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string;
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: MAP_API_KEY,
    libraries,
  });

  const getGroupPin = async () => {
    const pinList = await getPinList(groupId);
    return pinList;
  };

  const { data, isLoading } = useQuery({
    queryKey: ["initial-map"],
    queryFn: async () => await getGroupPin(),
  });
  const dispatch = useAppDispatch();
  dispatch(setGroupId(groupId));

  return isLoaded ? <Map markerList={data} /> : <div>로딩중입니다.</div>;
};

export default MapUpper;
