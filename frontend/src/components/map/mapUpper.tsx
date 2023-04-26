"use client";

import React from "react";
import { useLoadScript } from "@react-google-maps/api";
import Map from "./Map";

const MapUpper = () => {
  const MAP_API_KEY = process.env.NEXT_PUBLIC_GOOGLE_MAP_API_KEY as string;
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: MAP_API_KEY,
  });

  return isLoaded ? <Map /> : null;
};

export default MapUpper;
