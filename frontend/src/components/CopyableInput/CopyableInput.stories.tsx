import type { Meta, StoryObj } from "@storybook/react-vite";
import CopyableInput from "./CopyableInput";

const meta: Meta<typeof CopyableInput> = {
  title: "Components/CopyableInput",
  component: CopyableInput,
  tags: ["autodocs"],
  parameters: {
    layout: "centered",
  },
  args: {
    value: "D'oh!",
  },
};
export default meta;

type Story = StoryObj<typeof CopyableInput>;

export const Normal: Story = {};
