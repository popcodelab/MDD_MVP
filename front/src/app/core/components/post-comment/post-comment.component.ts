import {Component, Input} from '@angular/core';
import {Comment} from "../../../features/posts/interfaces/comment";
import {BreakpointObserver} from "@angular/cdk/layout";
import {DatePipe, NgClass} from "@angular/common";

@Component({
  selector: 'app-post-comment',
  standalone: true,
  imports: [
    NgClass,
    DatePipe
  ],
  templateUrl: './post-comment.component.html',
  styleUrl: './post-comment.component.scss'
})
export class PostCommentComponent {
  @Input() comment! : Comment

  isLargeScreen$: boolean = this.breakpointObserver.isMatched('(min-width: 600px)');

  /**
   * Constructs a new instance of the PostCommentComponent class.
   *
   * The BreakpointObserver is a utility for checking if the current device's screen size matches against
   * some CSS media queries
   *
   * @param {BreakpointObserver} breakpointObserver - The breakpointObserver parameter used for determining the
   * device's current breakpoint.
   */
  constructor(private breakpointObserver: BreakpointObserver) {}
}
