app.value('QR_DATA',
    {
        emotions: {
            happy: {
                emotion: 'happy',
                styles: {
                    panel: 'happy',
                    button: 'happy'
                },
                questions: [
                    'You know what I love?',
                    'You know what I can\'t live without?',
                    'You know what\'s cool?',
                    'You know what makes me laugh?',
                    'You know what I\'m thankful for?',
                    'You know what makes me smile?',
                    'You know what inspires me?',
                    'You know what makes me happy?',
                    'You know what I like?',
                    'You know what I wish for?',
                    'You know what\'s pretty good?',
                    'You know what I like to think about?'
                ]
            },
            angry: {
                emotion: 'angry',
                styles: {
                    panel: 'danger',
                    button: 'angry'
                },
                questions: [
                    'You know what I hate?',
                    'You know what makes me angry?',
                    'You know what\'s bullshit?',
                    'You know what sucks?',
                    'You know what I don\'t like?',
                    'You know what annoys me?',
                    'You know what infuriates me?',
                    'You know what I can\'t stand?',
                    'You know what pisses me off?',
                    'You know what\'s unjust?'
                ]
            },
            sad: {
                emotion: 'sad',
                styles: {
                    panel: 'sad',
                    button: 'sad'
                },
                questions: [
                    'You know what makes me cry?',
                    'You know what\'s depressing?',
                    'You know what makes me sad?',
                    'You know what I wish had happened?',
                    'You know what sucks?',
                    'You know what I don\'t like thinking about?',
                    'You know what I miss?',
                    'You know what I regret?',
                    'You know what scares me the most?',
                    'You know what\'s the worst?'
                ]
            }
        },
        shareUrls: {
            facebook: 'http://www.facebook.com/share.php?u=https://quickrant.com&title=Express yourself without yourself. Say anything to everyone. No login required',
            twitter: 'http://twitter.com/intent/tweet?status=Express yourself without yourself. Say anything to everyone. No login required + https://quickrant.com',
            reddit: 'http://www.reddit.com/submit?url=https://quickrant.com&title=Express yourself without yourself. Say anything to everyone. No login required',
            google: 'https://plus.google.com/share?url=https://quickrant.com',
            tumblr: 'http://www.tumblr.com/share?v=3&u=https://quickrant.com&t=Express yourself without yourself. Say anything to everyone. No login required',
            stumbleupon: 'http://www.stumbleupon.com/submit?url=https://quickrant.com&title=Express yourself without yourself. Say anything to everyone. No login required'
            },
        notify: {
            beta: {
                title: 'Beta',
                body: 'Quickrant is currently in beta while I add functionality and fix bugs. <br><br> If you see any abnormal behavior, or know of a feature you\'d like to see, feel free to <a href="https://github.com/mikechabot/quickrant-web/issues">create an issue or enhancement</a> on GitHub.'
            },
            notFound: {
                body: 'It doesn\'t look as though there\'s a rant with id: ',
                title: 'Uh oh...'
            },
            noSession: {
                body: 'You\'ll need to enable cookies',
                title: 'Something went wrong...'
            }
        }
    });