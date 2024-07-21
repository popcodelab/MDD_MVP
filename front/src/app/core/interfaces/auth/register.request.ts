/**
 * Represents a request to register a new user.
 *
 * @interface
 */
export interface RegisterRequest {
  email: string;
  username: string;
  password: string;
}
