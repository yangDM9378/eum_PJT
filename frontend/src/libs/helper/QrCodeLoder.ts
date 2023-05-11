import QRCode from "qrcode";

export async function makeQrCode(text: string) {
  try {
    const qrCode = await QRCode.toDataURL(text);
    return qrCode;
  } catch (err) {
    console.error(err);
  }
}
