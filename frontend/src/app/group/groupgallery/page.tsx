"use client";

import React, { useState } from "react";
import Image from "next/image";

const GroupGallery = () => {
  const images: string[] = [
    "/images/gallery1.png",
    "/images/gallery2.png",
    "/images/gallery3.png",
    "/images/gallery1.png",
    "/images/gallery2.png",
    "/images/gallery3.png",
    "/images/gallery1.png",
    "/images/gallery2.png",
    "/images/gallery3.png",
    "/images/gallery1.png",
    "/images/gallery2.png",
    "/images/gallery3.png",
  ];
  const [angle, setAngle] = useState(0);

  const galleryspin = (sign: boolean) => {
    let newAngle = angle;
    if (!sign) {
      newAngle = newAngle + 45;
    } else {
      newAngle = newAngle - 45;
    }
    setAngle(newAngle);
  };

  return (
    <div className="bg-black h-[740px]">
      <div
        id="carousel"
        style={{
          paddingTop: "50%",
          perspective: "1200px",
          background: "#100000",
          fontSize: 0,
          overflow: "hidden",
        }}
      >
        <figure
          id="spinner"
          style={{
            transformStyle: "preserve-3d",
            height: "300px",
            transformOrigin: "50% 50% -500px",
            transition: "1s",
            transform: `rotateY(${angle}deg)`,
          }}
        >
          {images.map((image: string, i: number) => (
            <Image
              key={i}
              src={image}
              alt=""
              width={300}
              height={300}
              style={{
                width: "60%",
                paddingTop: "10%",
                maxWidth: "425px",
                position: "absolute",
                left: "30%",
                transformOrigin: "50% 50% -500px",
                outline: "1px solid transparent",
                transform: `rotateY(${-45 * i}deg)`,
              }}
            />
          ))}
        </figure>
      </div>
      <span
        style={{
          float: "left",
          color: "#fff",
          margin: "5%",
          display: "inline-block",
          textDecoration: "none",
          fontSize: "2rem",
          transition: "0.6s color",
          position: "relative",
          marginTop: "3rem",
          borderBottom: "none",
          lineHeight: 0,
        }}
        className="ss-icon"
        onClick={() => galleryspin(false)}
      >
        &lt;
      </span>
      <span
        style={{
          float: "right",
          color: "#fff",
          margin: "5%",
          display: "inline-block",
          textDecoration: "none",
          fontSize: "2rem",
          transition: "0.6s color",
          position: "relative",
          marginTop: "3rem",
          borderBottom: "none",
          lineHeight: 0,
        }}
        className="ss-icon"
        onClick={() => galleryspin(true)}
      >
        &gt;
      </span>
    </div>
  );
};

export default GroupGallery;
