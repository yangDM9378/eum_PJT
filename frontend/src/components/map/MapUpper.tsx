"use client";

import React, { useEffect } from "react";
import { useLoadScript, LoadScriptProps } from "@react-google-maps/api";
import Map from "./Map";
import { getPinList } from "@/services/pinApi";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useAppDispatch } from "@/redux/hooks";
import { setGroupId } from "@/redux/map/mapSlice";

const placeLibrary: LoadScriptProps["libraries"] = ["places"];

interface Props {
  groupId: number;
}
const MapUpper = ({ groupId }: Props) => {
  const MAP_API_KEY = process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string;
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: MAP_API_KEY,
    libraries: placeLibrary,
  });

  const getGroupPin = async () => {
    const pinList = await getPinList(groupId);
    return pinList;
  };
  const queryClient = useQueryClient();
  queryClient.invalidateQueries({ queryKey: ["initial-map"] });

  const { data, isLoading, isFetching, error } = useQuery({
    queryKey: ["initial-map"],
    queryFn: async () => await getGroupPin(),
  });

  const dispatch = useAppDispatch();
  dispatch(setGroupId(groupId));

  return isLoaded ? <Map markerList={data} /> : null;
};

export default MapUpper;
