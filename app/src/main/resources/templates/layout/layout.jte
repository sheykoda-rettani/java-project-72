@import gg.jte.Content

@param String title = "Home page"
@param Content content
@param Content flashMessage = null

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
    </nav>

    <main class="container">
        @if(flashMessage != null)
            <div class="alert alert-success">
                ${flashMessage}
            </div>
        @endif
        <div class="content">
            ${content}
        </div>
    </main>
</body>