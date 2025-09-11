import type { Meta, StoryObj } from "@storybook/react-vite";
import Title from "./Title";

const meta: Meta<typeof Title> = {
  title: "Components/Title",
  tags: ["autodocs"],
  component: Title,
  args: {
    content: "Stay hungry, stay foolish.",
  },
};
export default meta;

type Story = StoryObj<typeof Title>;

export const Default: Story = {};
export const Level1: Story = {
  args: {
    content: "Stay hungry, stay foolish.",
    level: "1",
  },
};
export const Level2: Story = {
  args: {
    content: "Stay hungry, stay foolish.",
    level: "2",
  },
};
export const Level3: Story = {
  args: {
    content: "Stay hungry, stay foolish.",
    level: "3",
  },
};
export const Level4: Story = {
  args: {
    content: "Stay hungry, stay foolish.",
    level: "4",
  },
};
export const Level5: Story = {
  args: {
    content: "Stay hungry, stay foolish.",
    level: "5",
  },
};
export const Level6: Story = {
  args: {
    content: "Stay hungry, stay foolish.",
    level: "6",
  },
};
