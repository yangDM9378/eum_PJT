// gps 위치 판별
const distance = (
  lat1: number,
  lon1: number,
  lat2: number,
  lon2: number
): number => {
  const R = 6371; // 지구 반지름 (단위: km)
  const dLat = ((lat2 - lat1) * Math.PI) / 180;
  const dLon = ((lon2 - lon1) * Math.PI) / 180;
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  const d = R * c; // 두 지점 간의 거리 (단위: km)
  return d * 1000; // 미터로 변환
};

const isWithin50m = (
  lat1: number,
  lon1: number,
  lat2: number,
  lon2: number
): boolean => {
  const d = distance(lat1, lon1, lat2, lon2);
  return d <= 1000;
};

export default isWithin50m;
