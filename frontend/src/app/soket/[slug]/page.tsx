import React, { useCallback, useRef, useState, useEffect } from "react";
import UserName from "@/components/socket/Socket";
const Soket = ({ params }: { params: { slug: string } }) => {
  return (
    <div>
      <UserName />
    </div>
  );
};

export default Soket;
