import { render, screen, fireEvent } from "@testing-library/react";
import { vi } from "vitest";
import QRCodeWithDownload from "./QRCodeWithDownload";

describe("QRCodeWithDownload", () => {
  beforeAll(() => {
    URL.createObjectURL = vi.fn(() => "blob:fake-url");
    URL.revokeObjectURL = vi.fn();
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it("renders the QR code", () => {
    render(<QRCodeWithDownload value="https://example.com" />);
    const svg = screen.getByRole("img", { hidden: true });
    expect(svg).toBeInTheDocument();
  });

  it("shows a download button", () => {
    render(<QRCodeWithDownload value="https://example.com" />);
    expect(screen.getByRole("button")).toBeInTheDocument();
  });

  it("creates and removes a download link when clicking the download qr button", () => {
    const appendSpy = vi.spyOn(document.body, "appendChild");
    const removeSpy = vi.spyOn(document.body, "removeChild");

    render(<QRCodeWithDownload value="https://example.com" />);
    fireEvent.click(screen.getByRole("button"));

    expect(appendSpy).toHaveBeenCalled();
    expect(removeSpy).toHaveBeenCalled();
    expect(URL.createObjectURL).toHaveBeenCalled();
    expect(URL.revokeObjectURL).toHaveBeenCalled();
  });
});
