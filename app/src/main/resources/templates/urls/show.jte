@import hexlet.code.dto.UrlPage

@import static hexlet.code.util.ViewUtils.PAGE_DATE_FORMATTER
@import static hexlet.code.util.ViewUtils.truncateWithEllipsis

@param UrlPage urlPage

@template.layout.layout(
    page = urlPage,
    content = @`
        <h1 class="display-4">Сайт: ${urlPage.getUrl().getName()}</h1>
        <a href="/urls" class="btn btn-secondary mb-3">Назад к списку</a>
        <table class="table-bordered w-100" data-test="url">
            <tbody>
                <tr>
                    <td>ID</td>
                    <td>${urlPage.getUrl().getId()}</td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td>${urlPage.getUrl().getName()}</td>
                </tr>
                <tr>
                    <td>Дата создания</td>
                    <td>${urlPage.getUrl().getCreatedAt().format(PAGE_DATE_FORMATTER)}</td>
                </tr>
            </tbody>
        </table>

        <h1 class="display-4">Проверки</h1>
        <form method="post" action="/urls/${urlPage.getUrl().getId()}/checks">
            <input type="submit" value="Запустить проверку" class="btn btn-primary mb-3">
        </form>
        <table class="table-bordered w-100" data-test="checks">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Код ответа</th>
                    <th>h1</th>
                    <th>title</th>
                    <th>description</th>
                    <th>Дата создания</th>
                </tr>
            </thead>
            <tbody>
                @for(var check : urlPage.getUrlChecks())
                    <tr>
                        <td>${check.getId()}</td>
                        <td>${check.getStatusCode()}</td>
                        <td>${truncateWithEllipsis(check.getH1())}</td>
                        <td>${truncateWithEllipsis(check.getTitle())}</td>
                        <td>${truncateWithEllipsis(check.getDescription())}</td>
                        <td>${check.getCreatedAt().format(PAGE_DATE_FORMATTER)}</td>
                    </tr>
                @endfor
            </tbody>
        </table>
    `
)