"use client";

type Props = {
  agingStart: boolean;
  beAgingImage: string;
};

const AgingList = (props: Props) => {
  const { agingStart, beAgingImage } = props;
  return (
    <>
      {agingStart ? (
        <div>{beAgingImage}</div>
      ) : (
        <div>에이징할 사진을 선택해주세요</div>
      )}
    </>
  );
};

export default AgingList;
