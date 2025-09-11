import type { Meta, StoryObj } from "@storybook/react-vite";
import Button from "./Button";
import { fn } from "storybook/test";

const meta: Meta<typeof Button> = {
  title: "Components/Button",
  component: Button,
  tags: ["autodocs"],
  parameters: {
    layout: "centered",
  },
  args: {
    label: "Button",
    onClick: fn(),
    isDisabled: false,
  },
};
export default meta;

type Story = StoryObj<typeof Button>;

export const Normal: Story = {};

export const Disabled: Story = {
  args: {
    isDisabled: true,
  },
};
