"use client";

import React, { useMemo, useState, useEffect } from "react";
import {
  GoogleMap,
  MarkerF,
  MarkerClustererF,
  Autocomplete,
} from "@react-google-maps/api";

import { Pin } from "@/types/pin";

// 지도 옵션입니다.
const GoogleMapOptions: google.maps.MapOptions = {
  tilt: 0,
  zoomControl: false,
  minZoom: 3,
  maxZoom: 19,
  streetViewControl: false,
  disableDefaultUI: true,
  gestureHandling: "greedy",
  styles: [
    {
      featureType: "poi",
      elementType: "all",
      stylers: [{ visibility: "off" }],
    },
  ],
};

interface Props {
  markerList: Array<Pin> | undefined;
}

const MainMap = ({ markerList }: Props) => {
  // 지도 상태 값
  const [mapCenter, setMapCenter] = useState({
    lat: 36.28179677050536,
    lng: 127.7571724919414,
  });

  //지도가 로드되면 매핑합니다.
  const [mapref, setMapRef] = useState<google.maps.Map | null>(null);
  const handleOnLoad = (map: google.maps.Map) => {
    setMapRef(map);
  };

  return (
    <section className="h-[100%] w-[50%] relative flex justify-center items-center">
      <GoogleMap
        onLoad={handleOnLoad}
        center={mapCenter}
        zoom={8}
        mapContainerStyle={{ width: "100%", height: "100%" }}
        options={GoogleMapOptions}
      >
        {/* <MarkerF position={{ lat: mapCenter.lat, lng: mapCenter.lng }} /> */}
        {/* 좌표 클러스터링 */}
        <MarkerClustererF minimumClusterSize={3}>
          {(clusterer) => (
            <>
              {markerList &&
                markerList.map((marker) => (
                  <MarkerF
                    key={marker.pinId}
                    position={{ lat: marker.latitude, lng: marker.longitude }}
                    clusterer={clusterer}
                    // onClick={() => clickMarker(marker.pinId)}
                  />
                ))}
            </>
          )}
        </MarkerClustererF>
      </GoogleMap>
    </section>
  );
};

export default React.memo(MainMap);
