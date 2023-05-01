"use client";

import React, { useMemo, useState, useEffect } from "react";
import {
  GoogleMap,
  MarkerF,
  MarkerClustererF,
  Autocomplete,
} from "@react-google-maps/api";
import EventOptionModal from "../modals/EventOptionModal";
import MessageModal from "../modals/MessageModal";
import { assign } from "@/redux/map/mapSlice";
import { useAppDispatch } from "@/redux/hooks";

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

function Map() {
  const [curCenter, setCurCenter] = useState({
    lat: 35.205331,
    lng: 126.811123,
  });

  const mapCenter = useMemo(
    () => ({ lat: 35.221305123331, lng: 126.811123 }),
    []
  );
  const [mapref, setMapRef] = useState<google.maps.Map | null>(null);
  const [changeCenter, setChangeCenter] = useState({
    lat: 35.205331,
    lng: 126.811123,
  });

  const [isOpen, setIsOpen] = useState(false);
  const [messageOpen, setMessageOpen] = useState(false);
  const [messageId, setMessageId] = useState(-1);

  const dispatch = useAppDispatch();

  // 검색기능입니다.

  // 함수입니다.
  //지도가 로드되면 매핑합니다.
  const handleOnLoad = (map: google.maps.Map) => {
    setMapRef(map);
  };

  // 지도를 움직일때 지도 중간 지점의 좌표입니다.
  const handleCenterChanged = () => {
    if (mapref) {
      const newCenter = mapref.getCenter();
      if (newCenter) {
        const center = { lat: newCenter.lat(), lng: newCenter.lng() };
        setChangeCenter(center);
      }
    }
  };

  // 메시지 마커 클릭 이벤트입니다.
  const clickMarker = (marker: {
    id: number;
    position: { lat: number; lng: number };
  }) => {
    setMessageId(marker.id);
    setMessageOpen(true);
  };

  // const options = {
  //   imagePath:
  //     "https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m1.png", // so you must have m1.png, m2.png, m3.png, m4.png, m5.png and m6.png in that folder
  // };

  // 메시지 추가 이벤트입니다. 버튼을 누르면 모달이 열립니다.
  const addLetter = () => {
    // alert(`추가버튼을 누르셨습니다. ${changeCenter.lat} ${changeCenter.lng}`);
    const addCoords = {
      lat: changeCenter.lat,
      lng: changeCenter.lng,
    };
    dispatch(assign(addCoords));
    setIsOpen(true);
  };

  const getUserGps = () => {
    navigator.geolocation.getCurrentPosition(function (position) {
      if (position) {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;
        setCurCenter({ lat, lng });
      }
    });
  };

  useEffect(() => {
    getUserGps();
  }, []);

  return (
    <section className="h-[75%] relative flex justify-center items-center">
      <GoogleMap
        onLoad={handleOnLoad}
        center={mapCenter}
        zoom={16}
        mapContainerStyle={{ width: "90%", height: "90%" }}
        options={GoogleMapOptions}
        onCenterChanged={handleCenterChanged}
      >
        <Autocomplete>
          <input
            type="text"
            style={{
              boxSizing: `border-box`,
              border: `1px solid transparent`,
              width: `240px`,
              height: `32px`,
              padding: `0 12px`,
              borderRadius: `3px`,
              boxShadow: `0 2px 6px rgba(0, 0, 0, 0.3)`,
              fontSize: `14px`,
              outline: `none`,
              textOverflow: `ellipses`,
              position: "absolute",
              left: "50%",
              marginLeft: "-120px",
              top: "2%",
            }}
          />
        </Autocomplete>
        {/* 지도의 센터 좌표 이미지입니다. */}
        <img
          className="absolute top-[50%] left-[50%] w-[10vh]"
          style={{ transform: "translate(-50%, -50%)" }}
          src="/map/centerTarget.png"
          alt="center"
        />
        {/* 좌표 클러스터링 */}
        <MarkerClustererF minimumClusterSize={3}>
          {(clusterer) => (
            <>
              {markers.map((marker) => (
                <MarkerF
                  key={marker.id}
                  position={marker.position}
                  clusterer={clusterer}
                  onClick={() => clickMarker(marker)}
                />
              ))}
            </>
          )}
        </MarkerClustererF>
        {/* 지도에 메시지 추가하기. */}
        <img
          className="absolute top-[90%] left-[85%] w-[13vh]"
          style={{ transform: "translate(-50%, -50%)" }}
          src="/map/plus.png"
          alt="center"
          onClick={addLetter}
        />
      </GoogleMap>
      <EventOptionModal
        isOpen={isOpen}
        setIsOpen={setIsOpen}
        changeCenter={changeCenter}
      />
      <MessageModal
        messageOpen={messageOpen}
        setMessageOpen={setMessageOpen}
        messageId={messageId}
      />
    </section>
  );
}

export default React.memo(Map);

const markers = [
  {
    id: 1,
    position: { lat: 35.203125331, lng: 126.8421111252 },
  },
  {
    id: 2,
    position: { lat: 35.220115123313, lng: 126.811114311 },
  },
  {
    id: 3,
    position: { lat: 35.2252412335, lng: 126.8111235 },
  },
  {
    id: 4,
    position: { lat: 35.1205436, lng: 126.8111236 },
  },
  {
    id: 5,
    position: { lat: 35.112320562, lng: 126.8115231 },
  },
  {
    id: 6,
    position: { lat: 35.25553314, lng: 126.814123212312 },
  },
  {
    id: 7,
    position: { lat: 35.221205331, lng: 126.814411233 },
  },
  {
    id: 8,
    position: { lat: 35.205331, lng: 126.8111231 },
  },
  {
    id: 9,
    position: { lat: 53.876, lng: 9.17 },
  },
  {
    id: 10,
    position: { lat: 53.345, lng: 9.23 },
  },
  {
    id: 11,
    position: { lat: 53.276, lng: 9.34 },
  },
];
