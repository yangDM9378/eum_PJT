import React, { useCallback, useRef, useState, useEffect } from "react";
import UserName from "@/components/socket/Socket";
const Soket = ({ params }: { params: { slug: number } }) => {
  return (
    <div>
      {params.slug}
      <UserName />
    </div>
  );
};

export default Soket;
