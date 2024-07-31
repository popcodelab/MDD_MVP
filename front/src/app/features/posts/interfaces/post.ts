export interface Post {
  id: number;
  title: string;
  content: string;
  userId: number;
  username: string;
  topicId: number;
  topicTitle: string;
  commentIds: number[];
  created_at: Date;
}
