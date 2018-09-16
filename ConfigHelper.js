process = () => {
    let names = $("mat-card > .parameter .content .name").map((i, e) => {
        return $(e).html()
    }).toArray()
    let descriptions = $("mat-card > .parameter .content").map((i, e) => {
        let desc = $(e).find(".description").html()
        if (desc) { return desc } else { return "" }
    }).toArray()
    let values = $("mat-card > .parameter .content .values").map((i, e) => {
        return $(e).find(".default-value .parameter-value").html()
    }).toArray()

    if (names.length != descriptions.length || names.length != values.length || descriptions.length != values.length) {
        console.log(`Critical script error, please report to author! names: ${names.length}, descriptions: ${descriptions.length}, values: ${values.length}`)
        return
    }

    let kotlinDataClass = []

    kotlinDataClass.push("@Serializable\n")
    kotlinDataClass.push("data class FiReKonf(\n")
    console.log(names)
    let structure = names.flatMap((name, index) => {
        let description = descriptions[index]
        let value = values[index]
        var fixedValue
        if (value.length > 60) {
            fixedValue = `"""${value}"""`
        } else {
            fixedValue = `"${value}"`
        }
        let result = [
            `    //${description}\n`,
            `    val ${name}: String = ${fixedValue}`
        ]
        if (index != names.length - 1) {
            result.push(",\n")
        }
        return result
    })
    structure.forEach(e => {
        kotlinDataClass.push(e)
    })


    kotlinDataClass.push("\n)")

    console.log(kotlinDataClass.join(""))
}