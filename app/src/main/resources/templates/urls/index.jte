@import hexlet.code.dto.UrlsPage

@import static hexlet.code.util.ViewUtils.PAGE_DATE_FORMATTER

@param UrlsPage urlsPage

@template.layout.layout(
page = urlsPage,
content = @`
    <h1 class="display-4">Список добавленных URL</h1>
    <a href="/" class="btn btn-secondary mb-3">Добавить новый URL</a>
    <table class="table-bordered w-100" data-test="urls">
        <thead>
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Дата создания</th>
            <th>Последняя проверка</th>
            <th>Код ответа</th>
        </tr>
        </thead>
        <tbody>
        @for(var url : urlsPage.getUrls())
            <tr>
                <td>${url.getId()}</td>
                <td><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                <td>${url.getCreatedAt().format(PAGE_DATE_FORMATTER)}</td>
                <td>${url.getLastCheckAt() == null ? null : url.getLastCheckAt().format(PAGE_DATE_FORMATTER)}</td>
                <td>${url.getLastStatus()}</td>
            </tr>
        @endfor
        </tbody>
    </table>
    @if(urlsPage.getUrls().isEmpty())
        <div class="alert alert-info">Список пуст. Добавьте первый URL!</div>
    @endif
`
)