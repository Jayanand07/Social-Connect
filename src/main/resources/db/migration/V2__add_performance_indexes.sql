-- ================================================================
-- V2: Performance indexes for Friends-Hub
-- These indexes fix full table scans on the most common queries.
-- ================================================================

-- Posts feed: ORDER BY created_at DESC (used on every home page load)
CREATE INDEX IF NOT EXISTS idx_posts_created_at
    ON posts (created_at DESC);

-- Posts by author (used on profile page)
CREATE INDEX IF NOT EXISTS idx_posts_user_id
    ON posts (user_id);

-- Unread notifications count (used in notification bell)
CREATE INDEX IF NOT EXISTS idx_notifications_user_read
    ON notifications (user_id, is_read, created_at DESC);

-- Chat messages lookup (sender/receiver/timestamp)
CREATE INDEX IF NOT EXISTS idx_chat_messages_dm
    ON chat_messages (sender_id, receiver_id, timestamp DESC);

-- Group chat messages
CREATE INDEX IF NOT EXISTS idx_group_messages_group
    ON chat_group_messages (group_id, created_at DESC);

-- Follow relationship lookup: "is user A following user B?"
CREATE INDEX IF NOT EXISTS idx_follows_pair
    ON follows (follower_id, following_id);

-- Stories (removed WHERE clause — NOW() not allowed in index predicates)
CREATE INDEX IF NOT EXISTS idx_stories_active
    ON stories (user_id, expires_at DESC);

-- Comments on a post
CREATE INDEX IF NOT EXISTS idx_comments_post
    ON comments (post_id, created_at ASC);

-- Reactions on a target (post/story/comment)
CREATE INDEX IF NOT EXISTS idx_reactions_target
    ON reactions (target_id, target_type);
