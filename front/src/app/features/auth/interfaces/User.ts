/**
 * Represents a user.
 *
 * @interface User
 * @property {number} id - The unique identifier for the user.
 * @property {string} email - The email address of the user.
 * @property {string} username - The username of the user.
 * @property {string} password - The password of the user.
 * @property {number[]} subscribedTopicIds - An array of topic IDs that the user is subscribed to.
 *
 * @author Pierre-Olivier Pignon
 * @version 1.0
 */
export interface User {
  id: number;
  email: string;
  username: string;
  password: string;
  subscribedTopicIds: number[];
}
