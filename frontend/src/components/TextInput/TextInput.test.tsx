import { vi } from "vitest";
import TextInput from "./TextInput";
import { fireEvent, render, screen } from "@testing-library/react";

describe("TextInput", () => {
  it("call onChange with new value when its value changes", () => {
    const onChangeHandler = vi.fn();
    const testValue = "testValue";
    const changedTestValue = "changedTestValue";

    render(<TextInput value={testValue} onChange={onChangeHandler} />);
    const inputElt = screen.getByRole("textbox");
    fireEvent.change(inputElt, { target: { value: changedTestValue } });

    expect(onChangeHandler).toHaveBeenCalledTimes(1);
    expect(onChangeHandler).toHaveBeenCalledWith(changedTestValue);
  });
});
