"use client";

import React, { useEffect, useState } from "react";
import { getGroupGallery } from "@/services/galleryApi";
import { useQuery } from "@tanstack/react-query";

const GroupGallery = ({ params }: { params: { slug: number } }) => {
  const groupID = params.slug;
  const getGallery = async () => {
    const picture = await getGroupGallery(groupID);
    return picture;
  };

  const { data, isLoading } = useQuery({
    queryKey: ["initial-gallery"],
    queryFn: getGallery,
  });
  console.log(data);
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
    <div className="flex flex-wrap justify-center">
      {data &&
        data.map((picture, idx) => (
          <img
            key={idx}
            src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${picture.image}`}
            alt=""
            className="w-[40%] rounded-md m-2"
          />
        ))}
    </div>
  );
};

export default GroupGallery;
