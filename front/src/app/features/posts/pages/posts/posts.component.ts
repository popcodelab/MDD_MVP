import {Component, OnDestroy, OnInit} from '@angular/core';
import {DatePipe, NgClass, NgForOf, TitleCasePipe} from "@angular/common";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from "@angular/material/card";
import {Post} from "../../interfaces/post";
import {Subscription, switchMap} from "rxjs";
import {PostService} from "../../services/post.service";
import {SessionService} from "../../../auth/services/session.service";
import {APP_CONSTANTS} from "../../../../shared/constants";
import {Topic} from "../../../topics/interfaces/topic";
import {MatDivider} from "@angular/material/divider";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatAnchor, MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [
    DatePipe,
    RouterLink,
    MatCard,
    MatCardHeader,
    MatCardContent,
    MatCardTitle,
    MatCardSubtitle,
    TitleCasePipe,
    MatDivider,
    MatMenu,
    MatMenuTrigger,
    MatAnchor,
    MatMenuItem,
    MatIcon,
    NgClass,
    MatButton,
    NgForOf
  ],
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss', '../../../../shared/styles/topic-card-styles.css']
})
export class PostsComponent implements OnInit, OnDestroy {
  /**
   * Represents the subscription for a PostService.
   */
  private postServiceSubscription: Subscription | null = null;

  sortBy: string | null = "date";
  sortDirection: string | null = null;
  sortDirections: { [key: string]: string } = {
    date: APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING,
    title: APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING,
  };
  ascendingOrder: boolean = true;
  posts: Post[] = [];

  /**
   *
   * @param {ActivatedRoute} route - The ActivatedRoute object that provides access to the current route information.
   * @param {Router} router - The Router object used for navigation and URL manipulation.
   * @param {PostService} postService - The PostService object used for interacting with post-related data and functionality.
   * @param {SessionService} sessionService - The SessionService object used for managing user session data.
   */
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService,
    private sessionService: SessionService
  ) {
  }

  trackByPostId(index: number, post: any): number {
    return post.id;
  }

  /**
   * Retrieves all posts related to the subscribed topics of the user.
   *
   * @private
   * @returns {void}
   */
  private getTopicUserPosts(): void {
    this.postServiceSubscription = this.sessionService.topicSubscriptions$.pipe(
      switchMap((topics: Topic[]) => this.postService.getAllTopicPosts(topics.map((topic: Topic) => topic.id))),
      switchMap(() => this.postService.posts$)
    ).subscribe((posts: Post[]) => {
      this.posts = posts;
    });
  }

  /**
   * Retrieves the query parameters from the current route and assigns them to the corresponding class variables.
   *
   * @private
   * @return {void} This method does not return anything.
   */
  private getQueryParams(): void {
    this.route.queryParams.subscribe(params => {
      if (params) {
        if (params['sort']) {
          this.sortBy = params['sort'];
        }
        this.sortDirection = params['direction'];
        if (!this.sortBy || !this.sortDirection) {
          this.swapSortOrder('date', 'desc');
        }
      }
    });
  }

  /**
   * Handles the click event when sorting is triggered.
   *
   * @param {string} sortBy - The field to sort by.
   * @param {string} direction - The direction of the sort. This can be "ascending" or "descending".
   *
   * @return {void}
   */
  onSortClick(sortBy: string, direction: string): void {
    console.debug("onSortClick - sortBy : " + sortBy + " - direction : " + direction);
    console.table(this.sortDirections);
    this.sortDirections[sortBy] = this.toggleSortDirection(direction);
    this.swapSortOrder(sortBy, this.sortDirections[sortBy]);

    this.ascendingOrder = this.sortDirections[sortBy] === APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING;
  }

  /**
   * Toggles the sort direction based on the current direction.
   *
   * @param {string} currentDirection - The current sort direction.
   * @returns {string} - The toggled sort direction.
   */
  private toggleSortDirection(currentDirection: string): string {
    return currentDirection === APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING ? APP_CONSTANTS.SORT_DIRECTIONS.DESCENDING
      : APP_CONSTANTS.SORT_DIRECTIONS.ASCENDING;
  }

  /**
   * Swaps the sort order by updating the query parameters in the URL and sorting the posts.
   *
   * @param {string} sortBy - The field to sort the posts by.
   * @param {string} direction - The direction of the sorting (asc or desc).
   * @private
   * @return {void}
   */
  private swapSortOrder(sortBy: string, direction: string): void {
    console.debug("swapSortOrder : sortBy : " + sortBy + " - direction : " + direction);
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {sort: sortBy, direction: direction},
      queryParamsHandling: 'merge'
    }).then(() => {
      this.postService.sortPostsBy(sortBy, direction);
      this.sortBy = sortBy;
    });
  }

  /**
   * Navigates to the '/posts/new' route.
   *
   * @return {void}
   */
  public newPost() {
    this.router.navigate(['/posts/new']);
  }

  /**
   * Initializes the component.
   *
   * This method is called when the component is initialized. It calls two other methods to fetch query parameters and user posts related to a topic.
   *
   * @return {void} Returns nothing.
   */
  ngOnInit(): void {
    this.getQueryParams();
    this.getTopicUserPosts();
  }

  /**
   * Lifecycle hook that is called when the component is being destroyed.
   * It is recommended to perform any necessary cleanup operations here,
   * such as unsubscribing from subscriptions or releasing resources.
   *
   * @return {void}
   */
  ngOnDestroy(): void {
    if (this.postServiceSubscription) {
      this.postServiceSubscription.unsubscribe();
    }
  }

}
