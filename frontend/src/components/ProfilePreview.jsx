import { useAuth } from '../context/AuthContext';
import { useEffect, useState } from 'react';
import { getProfile, getSuggestions, followUser } from '../api/users';
import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';

export default function ProfilePreview() {
    const { user } = useAuth();
    const [profile, setProfile] = useState(null);
    const [suggestions, setSuggestions] = useState([]);
    const [loadingSuggestions, setLoadingSuggestions] = useState(true);

    useEffect(() => {
        getProfile().then((res) => setProfile(res.data)).catch(() => { });
        getSuggestions()
            .then((res) => {
                setSuggestions(res.data);
                setLoadingSuggestions(false);
            })
            .catch(() => setLoadingSuggestions(false));
    }, []);

    const handleFollow = async (userId) => {
        try {
            await followUser(userId);
            setSuggestions(prev => prev.filter(u => u.id !== userId));
        } catch (err) {
            console.error('Follow failed', err);
        }
    };

    const initial = user?.email?.charAt(0)?.toUpperCase() || '?';
    const displayName = profile ? `${profile.firstName || ''} ${profile.lastName || ''}`.trim() : '';
    const profilePic = profile?.profilePicUrl;

    return (
        <div className="hidden xl:block w-[320px] h-screen sticky top-0 p-5">
            <motion.div
                initial={{ opacity: 0, x: 20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: 0.3 }}
                className="pt-4"
            >
                {/* Profile card — IG style */}
                <div className="flex items-center gap-3 mb-6">
                    <div className="avatar-story">
                        <div className="avatar w-[56px] h-[56px] text-[18px]">
                            {profilePic ? (
                                <img src={profilePic} alt={displayName} className="w-full h-full object-cover rounded-full" />
                            ) : initial}
                        </div>
                    </div>
                    <div className="min-w-0">
                        <p className="text-[14px] font-semibold text-[var(--text-primary)] truncate">{displayName}</p>
                        <p className="text-[13px] text-[var(--text-muted)] truncate">@{user?.email?.split('@')[0]}</p>
                    </div>
                </div>

                {/* Suggestions header */}
                <div className="flex items-center justify-between mb-3">
                    <span className="text-[13px] font-semibold text-[var(--text-muted)]">Suggested for you</span>
                    <button className="text-[12px] font-semibold text-[var(--text-primary)] hover:text-[var(--text-muted)] cursor-pointer transition-colors">See All</button>
                </div>

                {/* Suggestion real data */}
                <div className="space-y-3">
                    {loadingSuggestions ? (
                        <div className="py-2 text-[12px] text-[var(--text-muted)]">Loading...</div>
                    ) : suggestions.length > 0 ? (
                        suggestions.map(suggestedUser => (
                            <div key={suggestedUser.id} className="flex items-center gap-3 py-1">
                                <Link to={`/profile/${suggestedUser.id}`}>
                                    <div className="avatar w-8 h-8 text-[10px]">
                                        {suggestedUser.profilePicUrl ? (
                                            <img src={suggestedUser.profilePicUrl} alt={suggestedUser.name} className="w-full h-full object-cover rounded-full" />
                                        ) : suggestedUser.name?.charAt(0)?.toUpperCase()}
                                    </div>
                                </Link>
                                <div className="flex-1 min-w-0">
                                    <Link to={`/profile/${suggestedUser.id}`}>
                                        <p className="text-[13px] font-semibold text-[var(--text-primary)] truncate hover:underline">{suggestedUser.name}</p>
                                    </Link>
                                    <p className="text-[11px] text-[var(--text-muted)] truncate">Suggested for you</p>
                                </div>
                                <button 
                                    onClick={() => handleFollow(suggestedUser.id)}
                                    className="text-[12px] font-semibold text-[var(--accent)] hover:text-[var(--accent-hover)] cursor-pointer transition-colors"
                                >
                                    Follow
                                </button>
                            </div>
                        ))
                    ) : (
                        <div className="py-2 text-[12px] text-[var(--text-muted)]">No suggestions</div>
                    )}
                </div>

                {/* Footer */}
                <div className="mt-6 text-[11px] text-[var(--text-muted)]/50 leading-relaxed">
                    <p>
                        <span style={{ fontFamily: "'Grand Hotel', cursive", fontSize: '18px' }}>
                            <span style={{ color: 'var(--text-primary)' }}>Friends</span>
                            <span className="text-[var(--accent)]">Hub</span>
                        </span> © 2026
                    </p>
                </div>
            </motion.div>
        </div>
    );
}
