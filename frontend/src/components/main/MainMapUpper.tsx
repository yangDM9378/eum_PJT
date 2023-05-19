"use client";

import React from "react";
import { useLoadScript, LoadScriptProps } from "@react-google-maps/api";
import MainMap from "./MainMap";
import { Pin } from "@/types/pin";
import Loading from "../common/Loading";

const libraries: LoadScriptProps["libraries"] = ["places"];

interface Props {
  markerList: Array<Pin> | undefined;
}

const MapUpper = ({ markerList }: Props) => {
  const MAP_API_KEY = process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string;
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: MAP_API_KEY,
    libraries,
  });

  return isLoaded ? (
    <MainMap markerList={markerList} />
  ) : (
    <div className="w-[100vw] h-[100vh] flex items-center justify-center">
      <Loading />
    </div>
  );
};

export default MapUpper;
