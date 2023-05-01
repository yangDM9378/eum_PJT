"use client";

import React from "react";
import { useLoadScript, LoadScriptProps } from "@react-google-maps/api";
import Map from "./Map";

const MapUpper = () => {
  const placeLibrary: LoadScriptProps["libraries"] = ["places"];
  const MAP_API_KEY = process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string;
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: MAP_API_KEY,
    libraries: placeLibrary,
  });

  return isLoaded ? <Map /> : null;
};

export default MapUpper;
