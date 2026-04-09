export const getUUID32 = (id? :string) => {
    const uuid = makeUUID32();
    return id? `${id}_${uuid}` : uuid

}

function makeUUID32(){
    return 'xxxxxxxxxxxx4xxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 32 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(32)
    })
}