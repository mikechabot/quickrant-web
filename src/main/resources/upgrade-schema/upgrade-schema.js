db.getCollection('rant').find({}).forEach(
    function(doc) {

        var selection = doc.selection;
        if (selection) {
            doc.emotion = doc.selection.emotion;
            doc.question = doc.selection.question;
            delete doc.selection;
        }

        var ranter = doc.ranter;
        if (ranter) {
            doc.name = doc.ranter.name;
            doc.location = doc.ranter.location;
            delete doc.ranter;
        }

        var comments = doc.comments;
        if (comments && comments.length > 0) {
            comments.forEach(function(comment) {
                var text = comment.comment;
                if (text) {
                    comment.text = comment.comment;
                    delete comment.comment;
                }
            });
        }

        var text = doc.rant;
        if (text) {
            doc.text = text;
            delete doc.rant;
        }

        db.getCollection('rant').save(doc);
    }
);

db.getCollection('session').find({}).forEach(
    function(doc) {

        var value = doc.cookieValue;
        if (value) {
            doc.cookie = {
                name: 'quickrant-uuid',
                value: value
            };
            delete doc.cookieValue;
        }

        db.getCollection('session').save(doc);
    }
);