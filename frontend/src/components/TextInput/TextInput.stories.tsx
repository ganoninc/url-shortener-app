import type { Meta, StoryObj } from "@storybook/react-vite";
import TextInput from "./TextInput";
import { action } from "storybook/actions";

const meta: Meta<typeof TextInput> = {
  title: "Components/TextInput",
  component: TextInput,
  tags: ["autodocs"],
  args: {
    value: "Dura lex, sed lex",
    placeholder: "This is placeholder",
    onChange: action("onChange"),
    isDisabled: false,
    validationState: "normal",
  },
};
export default meta;

type Story = StoryObj<typeof TextInput>;

export const Empty: Story = {
  args: {
    value: "",
  },
};

export const Normal: Story = {
  args: {},
};

export const IsValid: Story = {
  args: {
    validationState: "valid",
  },
};

export const HasError: Story = {
  args: {
    validationState: "error",
  },
};

export const Disabled: Story = {
  args: {
    isDisabled: true,
  },
};
