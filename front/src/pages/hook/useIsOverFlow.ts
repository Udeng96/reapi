import * as React from 'react';


export const useIsOverflow = (ref:React.Ref<any>, callback:Function|null = null) => {
    const [isOverflow, setIsOverflow] = React.useState(false);

    React.useLayoutEffect(() => {
        // @ts-ignore
        const { current } = ref;

        const trigger = () => {
            const hasOverflow = current.scrollWidth > current.clientWidth;
            setIsOverflow(hasOverflow);

            if (callback) callback(hasOverflow);
        };

        if (current) {
            trigger();
        }
    }, [callback, ref]);

    return isOverflow;
};


export const useIsOverflow2Item = (ref1:React.Ref<any>,ref2:React.Ref<any>, callback:Function|null = null) => {
    const [isOverflow, setIsOverflow] = React.useState(false);

    React.useLayoutEffect(() => {
        // @ts-ignore
        const { current :c1 } = ref1;
        // @ts-ignore
        const { current :c2 } = ref2;

        const trigger = () => {
            // console.log(c1.scrollWidth,c2.clientWidth)
            const hasOverflow = c1.scrollWidth > c2.clientWidth;

            setIsOverflow(hasOverflow);

            if (callback) callback(hasOverflow);
        };

        if (c1 && c2) {
            trigger();
        }
    }, [callback, ref1 ,ref2]);

    return isOverflow;
};