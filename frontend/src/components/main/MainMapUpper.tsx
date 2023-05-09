"use client";

import React from "react";
import { useLoadScript, LoadScriptProps } from "@react-google-maps/api";
import MainMap from "./MainMap";

const libraries: LoadScriptProps["libraries"] = ["places"];

const MapUpper = () => {
  const MAP_API_KEY = process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string;
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: MAP_API_KEY,
    libraries,
  });

  return isLoaded ? <MainMap /> : <div>로딩중입니다.</div>;
};

export default MapUpper;
