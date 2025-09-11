import type { Meta, StoryObj } from "@storybook/react-vite";
import Text from "./Text";

const meta: Meta<typeof Text> = {
  title: "Components/Text",
  component: Text,
  tags: ["autodocs"],
  args: {
    content: "Life is what happens while you’re busy making other plans",
  },
};
export default meta;

type Story = StoryObj<typeof Text>;

export const Default: Story = {};
