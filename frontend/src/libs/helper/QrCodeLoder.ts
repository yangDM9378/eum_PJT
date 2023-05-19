import QRCode from "qrcode";

export async function makeQrCode(text: string): Promise<string | null> {
  try {
    const qrCode = await QRCode.toDataURL(text);
    return qrCode;
  } catch (err) {
    return null;
  }
}
