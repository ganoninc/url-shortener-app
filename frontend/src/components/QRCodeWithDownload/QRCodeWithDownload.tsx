import QRCode from "react-qr-code";
import styles from "./QRCodeWithDownload.module.css";
import { useRef } from "react";
import Button from "../Button/Button";
import toast from "react-hot-toast";

type QRCodeWithDownloadProps = {
  value: string;
};

export default function QRCodeWithDownload({ value }: QRCodeWithDownloadProps) {
  const svgRef = useRef(null);

  function handleDownload() {
    const svg = svgRef.current as SVGElement | null;
    if (!svg) return;

    const serializer = new XMLSerializer();
    const svgData = serializer.serializeToString(svg);
    const blob = new Blob([svgData], { type: "image/svg+xml;charset=utf-8" });
    const url = URL.createObjectURL(blob);

    const downloadLink = document.createElement("a");
    downloadLink.href = url;
    downloadLink.download = "QRCode.svg";

    document.body.appendChild(downloadLink);
    downloadLink.click();

    document.body.removeChild(downloadLink);
    URL.revokeObjectURL(url);

    toast.success("QR code downloaded!");
  }

  return (
    <div className={styles.container}>
      <QRCode
        role="img"
        aria-label="QR Code"
        ref={svgRef}
        size={256}
        value={value}
        style={{ height: "auto", maxWidth: "100%", width: "100%" }}
        viewBox={`0 0 256 256`}
      />
      <Button label="Download QR code" onClick={handleDownload} />
    </div>
  );
}
