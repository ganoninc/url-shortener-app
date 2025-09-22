import { vi } from "vitest";
import CopyableInput from "./CopyableInput";
import { fireEvent, render, screen } from "@testing-library/react";

describe("CopyableInput", () => {
  it("copies to clipboard its value", () => {
    const writeTextMock = vi.fn();
    Object.defineProperty(navigator, "clipboard", {
      value: {
        writeText: writeTextMock,
      },
      writable: true,
    });

    window.alert = vi.fn();
    const testValue = "testValue";

    render(<CopyableInput value={testValue} />);
    const btnElt = screen.getByRole("button");
    fireEvent.click(btnElt);

    expect(writeTextMock).toHaveBeenCalledTimes(1);
    expect(writeTextMock).toHaveBeenCalledWith(testValue);
  });
});
