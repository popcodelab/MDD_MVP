export interface Comment {
  id: number;
  content: string;
  userId: number;
  username: string;
  postId: number;
  created_at: Date;
}
